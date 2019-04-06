const ONCE : AddEventListenerOptions = {
  once: true,
  passive: true
};
const SEVERAL_TIMES : AddEventListenerOptions = {
  once: false,
  passive: true
};

const xhr = new XMLHttpRequest ();
let timeout: number | undefined;
let previousValue = '';

function createHTMLLIElement (content: string): HTMLLIElement {
  const li = <HTMLLIElement>document.createElementNS ('http://www.w3.org/1999/xhtml', 'li');
  li.innerText = content;
  return li;
}

function listSuggestions () {
  if (xhr.status === 200) {
    const result = <string[]>JSON.parse (xhr.response);
    const list = <HTMLOListElement>document.getElementById ('auto-complete-list');
    result.map (createHTMLLIElement).forEach (li => {
      list.appendChild (li);
    });
    list.hidden = false;
  }
}

function sendRequest (url: URL) {
  xhr.open ('GET', url.toString ());
  xhr.setRequestHeader ('Accept', 'application/json');
  xhr.addEventListener ('load', listSuggestions, ONCE);
  xhr.send ();
}

function clear () {
  window.clearTimeout (timeout);
  previousValue = '';
  xhr.abort ();
  const list = <HTMLOListElement>document.getElementById ('auto-complete-list');
  list.hidden = true;
  while (list.hasChildNodes()) {
    list.removeChild (list.firstChild!);
  }
}

function autoComplete (event: KeyboardEvent) {
  const input = <HTMLInputElement>event.target;
  const path = <string>input.dataset['autoCompletePath'];

  const list = <HTMLOListElement>document.getElementById ('auto-complete-list');
  list.style.setProperty ('left', input.offsetLeft + 'px');
  list.style.setProperty ('top', (input.offsetTop + input.offsetHeight) + 'px');
  clear ();

  if (event.key !== 'Escape' && input.value !== '' && input.value !== previousValue) {
    const url = new URL (path, document.baseURI);
    url.searchParams.append ('q', input.value);
    previousValue = input.value;
    timeout = window.setTimeout (sendRequest, 800, url);
  }
}

export function enableAutoCompletion () {
  const autoCompletableInputs = <NodeListOf<HTMLInputElement>>document.querySelectorAll ('input[data-auto-complete-path]');
  autoCompletableInputs.forEach ((input) => {
    input.autocomplete = 'off';
    input.addEventListener ('keyup', autoComplete, SEVERAL_TIMES);
    input.addEventListener ('blur', clear, SEVERAL_TIMES);
  });
}
