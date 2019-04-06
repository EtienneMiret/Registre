const ONCE : AddEventListenerOptions = {
  once: true,
  passive: true
};
const SEVERAL_TIMES : AddEventListenerOptions = {
  once: false,
  passive: true
};

function createHTMLLIElement (content: string): HTMLLIElement {
  const li = <HTMLLIElement>document.createElementNS ('http://www.w3.org/1999/xhtml', 'li');
  li.innerText = content;
  return li;
}

function listSuggestions (event: Event) {
  const xhr = <XMLHttpRequest>event.target;
  if (xhr.status === 200) {
    const result = <string[]>JSON.parse (xhr.response);
    const list = <HTMLOListElement>document.getElementById ('auto-complete-list');
    while (list.hasChildNodes()) {
      list.removeChild (list.firstChild!);
    }
    result.map (createHTMLLIElement).forEach (li => {
      list.appendChild (li);
    });
    list.hidden = false;
  }
}

function autoComplete (event: Event) {
  const input = <HTMLInputElement>event.target;
  const xhr = new XMLHttpRequest ();
  const path = <string>input.dataset['autoCompletePath'];
  const url = new URL (path, document.baseURI);
  url.searchParams.append ('q', input.value);
  xhr.open ('GET', url.toString ());
  xhr.setRequestHeader ('Accept', 'application/json');
  xhr.addEventListener ('load', listSuggestions, ONCE);
  xhr.send ();

  const list = <HTMLOListElement>document.getElementById ('auto-complete-list');
  const inputRect = input.getBoundingClientRect();
  list.hidden = true;
  list.style.setProperty ('left', input.offsetLeft + 'px');
  list.style.setProperty ('top', (input.offsetTop + input.offsetHeight) + 'px');
}

export function enableAutoCompletion () {
  const autoCompletableInputs = <NodeListOf<HTMLInputElement>>document.querySelectorAll ('input[data-auto-complete-path]');
  autoCompletableInputs.forEach ((input) => {
    input.autocomplete = 'off';
    input.addEventListener ('keyup', autoComplete, SEVERAL_TIMES);
  });
}
