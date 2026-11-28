import {enableSearchFieldsManagement} from './fields-management.js';

function ready() {
  enableSearchFieldsManagement();
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', ready, {
    once: true,
    passive: true,
  });
} else {
  ready();
}
