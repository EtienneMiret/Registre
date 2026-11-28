import { enableEditorFieldsManagement } from './fields-management.js';

function ready () {
  enableEditorFieldsManagement ();
}

if (document.readyState === 'loading') {
  document.addEventListener ('DOMContentLoaded', ready, {
    once: true,
    passive: true
  });
} else {
  ready ();
}
