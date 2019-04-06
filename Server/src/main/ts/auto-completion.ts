const ONCE : AddEventListenerOptions = {
  once: true,
  passive: true
};
const SEVERAL_TIMES : AddEventListenerOptions = {
  once: false,
  passive: true
};
const ACTIVE: AddEventListenerOptions = {
  once: false,
  passive: false
};

const xhr = new XMLHttpRequest ();
let timeout: number | undefined;
let previousValue = '';
let currentLi: HTMLLIElement | undefined;

function getList () {
  return <HTMLOListElement>document.getElementById ('auto-complete-list');
}

function createHTMLLIElement (content: string): HTMLLIElement {
  const li = <HTMLLIElement>document.createElementNS ('http://www.w3.org/1999/xhtml', 'li');
  li.innerText = content;
  return li;
}

function listSuggestions () {
  if (xhr.status === 200) {
    const result = <string[]>JSON.parse (xhr.response);
    const list = getList ();
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
  currentLi = undefined;
  xhr.abort ();
  const list = getList ();
  list.hidden = true;
  while (list.hasChildNodes()) {
    list.removeChild (list.firstChild!);
  }
}

function autoComplete (input: HTMLInputElement) {
  clear ();
  const list = getList ();
  list.style.setProperty ('left', input.offsetLeft + 'px');
  list.style.setProperty ('top', (input.offsetTop + input.offsetHeight) + 'px');

  if (input.value !== '' && input.value !== previousValue) {
    const path = <string>input.dataset['autoCompletePath'];
    const url = new URL (path, document.baseURI);
    url.searchParams.append ('q', input.value);
    previousValue = input.value;
    timeout = window.setTimeout (sendRequest, 800, url);
  }
}

function moveOn (event: KeyboardEvent) {
  const input = <HTMLInputElement>event.target;
  switch (event.key) {
    case 'ArrowDown':
      if (currentLi) {
        if (currentLi.nextSibling) {
          currentLi.classList.remove ('active');
          currentLi = <HTMLLIElement>currentLi.nextSibling;
          currentLi.classList.add ('active');
        }
      } else {
        const list = getList ();
        if (list.hasChildNodes()) {
          currentLi = <HTMLLIElement>list.firstChild;
          currentLi.classList.add ('active');
        } else {
          autoComplete (input);
        }
      }
      break;
    case 'ArrowUp':
      if (currentLi && currentLi.previousSibling) {
        currentLi.classList.remove ('active');
        currentLi = <HTMLLIElement>currentLi.previousSibling;
        currentLi.classList.add ('active');
      }
      break;
    case 'Tab':
    case 'Enter':
      if (currentLi) {
        event.preventDefault ();
        input.value = currentLi.innerText;
      }
      break;
  }
}

function keyDown (event: KeyboardEvent) {
  const input = <HTMLInputElement>event.target;
  switch (event.key) {
    case 'Escape':
      clear ();
      break;
    case 'ArrowDown':
    case 'ArrowUp':
    case 'Enter':
    case 'Tab':
      moveOn (event);
      break;
    default:
      autoComplete (input);
      break;
  }
}

export function enableAutoCompletion () {
  const autoCompletableInputs = <NodeListOf<HTMLInputElement>>document.querySelectorAll ('input[data-auto-complete-path]');
  autoCompletableInputs.forEach ((input) => {
    input.autocomplete = 'off';
    input.addEventListener ('keydown', keyDown, ACTIVE);
    input.addEventListener ('blur', clear, SEVERAL_TIMES);
  });
}
