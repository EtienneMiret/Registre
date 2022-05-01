import { enableFieldsManagement } from './fields-management.js';

function ready () {
  enableFieldsManagement ();
}

if (document.readyState === 'loading') {
  document.addEventListener ('DOMContentLoaded', ready, {
    once: true,
    passive: true
  });
} else {
  ready ();
}
