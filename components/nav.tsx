import Link from 'next/link';
import Image from 'next/image';
import { NextComponentType } from 'next';
import styles from 'styles/Nav.module.scss';
import movie from 'public/movie.svg';
import comic from 'public/comic.svg';
import book from 'public/book.svg';
import search from 'public/search.svg';
import { signOut } from 'next-auth/react';

const Nav: NextComponentType = () => {
  return <nav className={styles.next}>
    <ul>
      <li><Link href="/"><a>Accueil</a></Link></li>
      <li><Link href="/Fiche/"><a>Ajouter une référence</a></Link></li>
      <li>
        <Link href="/Rechercher?q="><a>Toutes les références</a></Link>{' '}
        <Link href="/Rechercher?q=type%3Amovie"><a>
          <Image src={movie} alt="Films"/>
        </a></Link>{' '}
        <Link href="/Rechercher?q=type%3Acomic"><a>
          <Image src={comic} alt="BD"/>
        </a></Link>{' '}
        <Link href="/Rechercher?q=type%3Abook"><a>
          <Image src={book} alt="Livres"/>
        </a></Link>
      </li>
      <li>
        <form action="/Rechercher">
          <label>
            <input name="q" placeholder="Rechercher" type="search"/>
            <button><Image src={search} alt="Rechercher"/></button>
          </label>
        </form>
      </li>
      <li><button onClick={() => signOut()}>Déconnexion</button></li>
    </ul>
  </nav>
};

export default Nav;
