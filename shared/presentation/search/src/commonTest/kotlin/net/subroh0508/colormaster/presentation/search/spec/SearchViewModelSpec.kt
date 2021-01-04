package net.subroh0508.colormaster.presentation.search.spec

import io.kotest.core.test.TestCase
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.containExactlyInAnyOrder
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.subroh0508.colormaster.model.*
import net.subroh0508.colormaster.presentation.search.MockIdolColorsRepository
import net.subroh0508.colormaster.presentation.search.everyRand
import net.subroh0508.colormaster.presentation.search.everySearch
import net.subroh0508.colormaster.presentation.search.model.IdolColorListItem
import net.subroh0508.colormaster.presentation.search.model.ManualSearchUiModel
import net.subroh0508.colormaster.presentation.search.model.SearchParams
import net.subroh0508.colormaster.presentation.search.viewmodel.IdolSearchViewModel
import net.subroh0508.colormaster.test.TestScope
import net.subroh0508.colormaster.test.ViewModelSpec

class SearchViewModelSpec : ViewModelSpec() {
    private val observedUiModels: MutableList<ManualSearchUiModel> = mutableListOf()

    private val repository = MockIdolColorsRepository()

    lateinit var viewModel: IdolSearchViewModel

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)

        viewModel = IdolSearchViewModel(repository, TestScope()/* for JS Runtime */)
        observedUiModels.clear()

        viewModel.uiModel.onEach { observedUiModels.add(it) }
            .launchIn(TestScope())
    }

    private val randomIdols = listOf(
        IdolColor("Shimabara_Elena", "島原エレナ", HexColor("9BCE92")),
        IdolColor("Abe_nana", "安部菜々", HexColor("E64A79")),
        IdolColor("Kohinata_Miho", "小日向美穂", HexColor("C64796")),
        IdolColor("Sakuma_Mayu", "佐久間まゆ", HexColor("D1197B")),
        IdolColor("Hidaka_Ai", "日高愛", HexColor("E85786")),
        IdolColor("Nakatani_Iku", "中谷育", HexColor("F7E78E")),
        IdolColor("Taiga_Takeru", "大河タケル", HexColor("0E0C9F")),
        IdolColor("Kuzunoha_Amehiko", "葛之葉雨彦", HexColor("111721")),
        IdolColor("Suou_Momoko", "周防桃子", HexColor("EFB864")),
        IdolColor("Ichikawa_Hinana", "市川雛菜", HexColor("FFC639")),
    )

    private val byName = IdolName("久川")
    private val byNameIdols = listOf(
        IdolColor("Hisakawa_Nagi", "久川凪", HexColor("F7A1BA")),
        IdolColor("Hisakawa_Hayate", "久川颯", HexColor("7ADAD6")),
    )

    private val byBrand = Brands._876
    private val byBrandIdols = listOf(
        IdolColor("Hidaka_Ai", "日高愛", HexColor("E85786")),
        IdolColor("Mizutani_Eri", "水谷絵理", HexColor("00ADB9")),
        IdolColor("Akizuki_Ryo_876", "秋月涼", HexColor("B2D468")),
    )
    
    private val byBrandAndType = Brands._765 to Types.MILLION_LIVE.PRINCESS
    private val byBrandAndTypeIdols = listOf(
        IdolColor("Amami_Haruka", "天海春香", HexColor("E22B30")),
        IdolColor("Ganaha_Hibiki", "我那覇響", HexColor("01ADB9")),
        IdolColor("Kikuchi_Makoto", "菊地真", HexColor("515558")),
        IdolColor("Hagiwara_Yukiho", "萩原雪歩", HexColor("D3DDE9")),
    )

    private fun subject(block: () -> Unit): List<ManualSearchUiModel> {
        block()
        return observedUiModels
    }

    init {
        test("#rand: when repository#rand returns idols colors it should post ManualSearchUiModel with filled list") {
            repository.everyRand { randomIdols }

            subject(viewModel::loadRandom).also { models ->
                models should haveSize(3)
                models[0] should {
                    it.items should beEmpty()
                    it.error should beNull()
                    it.isLoading should be(false)
                }
                models[1] should {
                    it.items should beEmpty()
                    it.error should beNull()
                    it.isLoading should be(true)
                }
                models[2] should {
                    it.items should containExactlyInAnyOrder(randomIdols.map(::IdolColorListItem))
                    it.error should beNull()
                    it.isLoading should be(false)
                }
            }
        }

        test("#rand: when repository#rand raise Exception it should post ManualSearchUiModel with empty list") {
            val error = IllegalStateException()

            repository.everyRand { throw error }

            subject(viewModel::loadRandom).also { models ->
                models should haveSize(3)
                models[0] should {
                    it.items should beEmpty()
                    it.error should beNull()
                    it.isLoading should be(false)
                }
                models[1] should {
                    it.items should beEmpty()
                    it.error should beNull()
                    it.isLoading should be(true)
                }
                models[2] should {
                    it.items should beEmpty()
                    it.error should be(error)
                    it.isLoading should be(false)
                }
            }
        }

        test("#search: when search params is empty it should post ManualSearchUiModel with filled list") {
            repository.everyRand { randomIdols }

            subject(viewModel::search).also { models ->
                models should haveSize(3)
                models.last().items should containExactlyInAnyOrder(randomIdols.map(::IdolColorListItem))
            }
        }

        test("#search: when change idol name it should post ManualSearchUiModel with filled list") {
            val params = SearchParams.EMPTY.change(byName)

            repository.everySearch(expectIdolName = byName) { _, _, _ -> byNameIdols }

            subject { viewModel.searchParams = params }.also { models ->
                models should haveSize(4)
                models.last() should {
                    it.items should containExactlyInAnyOrder(byNameIdols.map(::IdolColorListItem))
                    it.params should be(params)
                }
            }
        }

        test("#search: when change brand it should post ManualSearchUiModel with filled list") {
            val params = SearchParams.EMPTY.change(byBrand)

            repository.everySearch(expectBrands = byBrand) { _, _, _ -> byBrandIdols }

            subject { viewModel.searchParams = params }.also { models ->
                models should haveSize(4)
                models.last() should {
                    it.items should containExactlyInAnyOrder(byBrandIdols.map(::IdolColorListItem))
                    it.params should be(params)
                }
            }
        }

        test("#search: when change brand and type it should post ManualSearchUiModel with filled list") {
            val (brand, type) = byBrandAndType
            val params = SearchParams.EMPTY.change(brand).change(type, checked = true)

            repository.everySearch(expectBrands = brand, expectTypes = setOf(type)) { _, _, _ -> byBrandAndTypeIdols }

            subject { viewModel.searchParams = params }.also { models ->
                models should haveSize(4)
                models.last() should {
                    it.items should containExactlyInAnyOrder(byBrandAndTypeIdols.map(::IdolColorListItem))
                    it.params should be(params)
                }
            }
        }

        test("#select: when change selected item it should post ManualSearchUiModel with select state") {
            repository.everyRand { randomIdols }

            subject {
                viewModel.loadRandom()
                viewModel.select(randomIdols[0], true)
                viewModel.select(randomIdols[0], false)
            }.also { models ->
                models should haveSize(5)
                models[models.lastIndex - 1] should {
                    it.items should haveSize(randomIdols.size)
                    it.selectedItems should be(listOf(randomIdols[0]))
                }
                models.last() should {
                    it.items should haveSize(randomIdols.size)
                    it.selectedItems should beEmpty()
                }
            }
        }

        test("#selectAll: when change selected item it should post ManualSearchUiModel with select state") {
            repository.everyRand { randomIdols }

            subject {
                viewModel.loadRandom()
                viewModel.selectAll(true)
                viewModel.selectAll(false)
            }.also { models ->
                models should haveSize(5)
                models[models.lastIndex - 1] should {
                    it.items should haveSize(randomIdols.size)
                    it.selectedItems should containExactlyInAnyOrder(randomIdols)
                }
                models.last() should {
                    it.items should haveSize(randomIdols.size)
                    it.selectedItems should beEmpty()
                }
            }
        }
    }
}
