function hideUnusedFields (form, type) {
  hideExcept (form, type, 'comic');
  hideExcept (form, type, 'movie');
  hideExcept (form, type, 'book');
}

function hideExcept (form, typeToKeep, type) {
  const elements = form.getElementsByClassName (type);
  for (let i = 0; i < elements.length; i++) {
    elements.item (i).hidden = typeToKeep !== type;
  }
}

function addActor (actorsList) {
  const index = actorsList
      .getElementsByTagNameNS ('http://www.w3.org/1999/xhtml', 'li')
      .length;
  const li = document.createElementNS ('http://www.w3.org/1999/xhtml', 'li');
  const input = document.createElementNS ('http://www.w3.org/1999/xhtml', 'input');
  input.name = `actors[${index}]`;
  li.appendChild (input);
  actorsList.appendChild (li);
}

function ready () {
  const form = document.getElementById ('form');
  hideUnusedFields (form, form.elements.type.value);
  document.getElementById ('add-actor').hidden = false;
}

ready ();
