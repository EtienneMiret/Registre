import { GetServerSidePropsResult, NextPageContext } from 'next';
import Image from 'next/image';
import { RegistreLayoutPage } from '../lib/types';
import { Artwork, ArtworkType } from '../lib/mongo/artwork/types';
import listArtworks from '../lib/mongo/artwork/list';
import { Series } from '../lib/mongo/series/types';
import listSeries from '../lib/mongo/series/list';

import movieIcon from '../public/movie.svg';
import comicIcon from '../public/comic.svg';
import bookIcon from '../public/book.svg';

const ICON_SIZE = 40;

interface SearchParams {
  results: Artwork[];
  series: Series[];
}

const Search: RegistreLayoutPage<SearchParams> = ({results, series}) => {
  const icon = function (type: ArtworkType) {
    switch (type) {
      case ArtworkType.Movie:
        return <Image src={movieIcon} alt="Film" height={ICON_SIZE} width={ICON_SIZE}/>;
      case ArtworkType.Comic:
        return <Image src={comicIcon} alt="Bande dessinée" height={ICON_SIZE} width={ICON_SIZE}/>;
      case ArtworkType.Book:
        return <Image src={bookIcon} alt="Livre" height={ICON_SIZE} width={ICON_SIZE}/>;
      default:
        return <></>;
    }
  }
  
  const line = function (artwork: Artwork) {
    const picture = artwork.picture ? <Image src={artwork.picture} alt=""/> : <></>;
    const seriesName = artwork.series ? series.find(s => s._id === artwork.series)?.name : '';

    return <tr>
      <td>{picture}</td>
      <td>{artwork.title}</td>
      <td>{seriesName}</td>
      <td>{artwork.season}</td>
      <td>{artwork.episode}</td>
      <td>{icon(artwork.type)}</td>
    </tr>;
  };

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
      <tbody>
      {results.map(artwork => line(artwork))}
      </tbody>
    </table>
  </>;
}

Search.title = 'Rechercher';

export default Search;

export async function getServerSideProps(context: NextPageContext): Promise<GetServerSidePropsResult<SearchParams>> {
  const results = await listArtworks();
  const seriesIDs = new Set(results.filter(a => a.series).map(a => a.series!));
  const series = await listSeries(seriesIDs);

  return {
    props: {
      results,
      series
    }
  };
}
