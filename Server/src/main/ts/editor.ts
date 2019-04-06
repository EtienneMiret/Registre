function hideUnusedFields (form: HTMLFormElement, type: string) {
  hideExcept (form, type, 'comic');
  hideExcept (form, type, 'movie');
  hideExcept (form, type, 'book');
}

function hideExcept (form: HTMLFormElement, typeToKeep: string, type: string) {
  const elements =
      <HTMLCollectionOf<HTMLElement>>form.getElementsByClassName (type);
  for (let i = 0; i < elements.length; i++) {
    elements.item (i)!.hidden = typeToKeep !== type;
  }
}

function addActor (actorsList: HTMLOListElement|HTMLUListElement) {
  const index = actorsList
      .getElementsByTagNameNS ('http://www.w3.org/1999/xhtml', 'li')
      .length;
  const li = document.createElementNS ('http://www.w3.org/1999/xhtml', 'li');
  const input =
      <HTMLInputElement>document.createElementNS ('http://www.w3.org/1999/xhtml', 'input');
  input.name = `actors[${index}]`;
  li.appendChild (input);
  actorsList.appendChild (li);
}

function ready () {
  const form = <HTMLFormElement>document.getElementById ('form');
  const typeSelector = <HTMLSelectElement>form.elements.namedItem ('type');
  const addActorButton = <HTMLButtonElement>document.getElementById ('add-actor');
  const actorsList = <HTMLUListElement>document.getElementById ('actors');
  hideUnusedFields (form, typeSelector.value);
  typeSelector.addEventListener ('change', () => hideUnusedFields (form, typeSelector.value), {
    once: false,
    passive: true
  });
  addActorButton.addEventListener ('click', () => addActor (actorsList), {
    once: false,
    passive: true
  });
  addActorButton.hidden = false;
}

if (document.readyState === 'loading') {
  document.addEventListener ('DOMContentLoaded', ready, {
    once: true,
    passive: true
  });
} else {
  ready ();
}
