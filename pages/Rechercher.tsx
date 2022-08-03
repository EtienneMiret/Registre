import { RegistreLayoutPage } from '../lib/types';

interface SearchParams {

}

const Search: RegistreLayoutPage<SearchParams> = ({}) => {
  return <>
    <table>
      <thead>
      <th/>
      <th>Référence</th>
      <th>Série</th>
      <th title="Saison">S</th>
      <th title="Épisode">É</th>
      <th/>
      </thead>
    </table>
  </>;
}

Search.title = 'Rechercher';

export default Search;
