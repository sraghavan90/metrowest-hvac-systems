function add_item(e)
{
    let option = e.target.selectedOptions[0];
    if (!option) return;

    let form = document.getElementById("order_form");
    let id_input = document.createElement("input");
    let qty_input = document.createElement("input");

    let next_index = form.querySelectorAll("input[data-product-id]").length;

    let name_prefix = "items["+next_index+"].";

    console.log("count:", next_index);

    id_input.setAttribute("type", "hidden");
    id_input.setAttribute("name", name_prefix + "id");
    id_input.value = option.value;

    qty_input.setAttribute("type", "number");
    qty_input.setAttribute("data-product-id", option.value)
    qty_input.setAttribute("name", name_prefix + "qty");
    qty_input.value = "1";

    let label = document.createElement("label");
    label.textContent = option.textContent;
    label.append(id_input);
    label.append(qty_input);
    form.prepend(document.createElement("br"));
    form.prepend(label);
}

function prepare_data()
{

}

document.getElementById("product_select").addEventListener("change", add_item);
document.getElementById("order_form").addEventListener("submit", add_item);