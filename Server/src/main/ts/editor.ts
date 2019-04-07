import { enableAutoCompletion } from './auto-completion.js';
import { enableFieldsManagement } from './fields-management.js';

function ready () {
  enableFieldsManagement ();
  enableAutoCompletion ();
}

if (document.readyState === 'loading') {
  document.addEventListener ('DOMContentLoaded', ready, {
    once: true,
    passive: true
  });
} else {
  ready ();
}
