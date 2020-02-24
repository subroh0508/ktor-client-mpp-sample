import { IdolColorActions } from 'ktor-client-mpp-sample-repository';

onload = async () => {
    const div = document.getElementById('main');

    const items = await new IdolColorActions().loadItems();

    items.forEach(item => {
        const box = document.createElement('div');

        div.appendChild(box);
        box.innerText = item.name + "\n" + item.color;
        box.style = `display: inline-block;width: 200px;height: 50px;margin: 2px;background-color: ${item.color};color: ${item.isBrighter ? "black" : "white"};text-align: center;`;
    });
};
