const INTERVAL = 1000;

const PASSIVE : AddEventListenerOptions = {
  once: false,
  passive: true
};
const ACTIVE: AddEventListenerOptions = {
  once: false,
  passive: false
};

const xhr = new XMLHttpRequest ();
xhr.addEventListener ('load', listSuggestions, PASSIVE);
let lastSentTime = new Date (0);
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
    currentLi = undefined;
    while (list.hasChildNodes ()) {
      list.removeChild (list.firstChild!);
    }
    result.map (createHTMLLIElement).forEach (li => {
      list.appendChild (li);
    });
    list.hidden = false;
  }
}

function sendRequest (input: HTMLInputElement) {
  if (input.value === '') {
    clear ();
  } else if (input.value !== previousValue) {
    window.clearTimeout (timeout);
    previousValue = input.value;
    lastSentTime = new Date ();
    timeout = undefined;
    const path = <string>input.dataset['autoCompletePath'];
    const url = new URL (path, document.baseURI);
    url.searchParams.append ('q', input.value);
    xhr.abort ();
    xhr.open ('GET', url.toString ());
    xhr.setRequestHeader ('Accept', 'application/json');
    xhr.send ();
  }
}

function clear () {
  window.clearTimeout (timeout);
  timeout = undefined;
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
  const list = getList ();
  list.style.setProperty ('left', input.offsetLeft + 'px');
  list.style.setProperty ('top', (input.offsetTop + input.offsetHeight) + 'px');

  const delta = new Date ().getTime () - lastSentTime.getTime ();
  if (delta >= INTERVAL) {
    sendRequest (input);
  } else if (timeout === undefined) {
    timeout = window.setTimeout (sendRequest, INTERVAL - delta, input);
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
          event.preventDefault ();
        }
      } else {
        const list = getList ();
        if (list.hasChildNodes()) {
          currentLi = <HTMLLIElement>list.firstChild;
          currentLi.classList.add ('active');
          event.preventDefault ();
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
        event.preventDefault ();
      }
      break;
    case 'Tab':
    case 'Enter':
      if (currentLi) {
        input.value = currentLi.innerText;
        clear ();
        event.preventDefault ();
      }
      break;
  }
}

const KEY_DOWN_KEYS = new Set<string> ([
  'Escape',
  'ArrowDown',
  'ArrowUp',
  'Enter',
  'Tab'
]);

function keyDown (event: KeyboardEvent) {
  if (KEY_DOWN_KEYS.has (event.key)) {
    if (event.key === 'Escape') {
      clear ();
    } else {
      moveOn (event);
    }
  }
}

function keyUp (event: KeyboardEvent) {
  if (!KEY_DOWN_KEYS.has (event.key)) {
    const input = <HTMLInputElement>event.target;
    autoComplete (input);
  }
}

function extractTargetLi (event: MouseEvent): HTMLLIElement | null {
  let target = <HTMLElement | null>event.target;
  while (target && (target.localName !== 'li' || target.namespaceURI !== 'http://www.w3.org/1999/xhtml')) {
    target = target.parentElement;
  }
  return <HTMLLIElement | null>target;
}

function mouseMove (event: MouseEvent) {
  const li = extractTargetLi (event);
  if (li === null) {
    return;
  }
  if (currentLi) {
    currentLi.classList.remove ('active');
  }
  currentLi = li;
  currentLi.classList.add ('active');
}

function click (event: MouseEvent) {
  const li = extractTargetLi (event);
  if (li
      && document.activeElement
      && document.activeElement.localName === 'input'
      && document.activeElement.namespaceURI === 'http://www.w3.org/1999/xhtml') {
    const input = <HTMLInputElement>document.activeElement;
    input.value = li.innerText;
    clear ();
  }
}

export function setAutoCompletable (input: HTMLInputElement) {
  input.autocomplete = 'off';
  input.addEventListener ('keydown', keyDown, ACTIVE);
  input.addEventListener ('keyup', keyUp, PASSIVE);
  input.addEventListener ('blur', clear, PASSIVE);
}

export function enableAutoCompletion () {
  const autoCompletableInputs = <NodeListOf<HTMLInputElement>>document.querySelectorAll ('input[data-auto-complete-path]');
  autoCompletableInputs.forEach (setAutoCompletable);
  const list = getList ();
  list.addEventListener ('mousemove', mouseMove, PASSIVE);
  list.addEventListener ('mousedown', click, PASSIVE);
}
