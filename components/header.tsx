import Link from 'next/link';
import Image from 'next/image';
import logo from 'public/logo.png';
import styles from 'styles/Header.module.scss';
import { NextComponentType } from 'next';

const Header: NextComponentType = () => {
  return <header className={styles.next}>
    <h1>
      <Link href="/"><a><Image src={logo} className={styles.logo} width={120} height={60} alt="Registre"/></a></Link>
    </h1>
  </header>;
};

export default Header;
