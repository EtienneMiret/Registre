import Image from 'next/image';
import logo from 'public/logo.png';
import { NextPage } from 'next';
import { signIn } from 'next-auth/react';
import styles from 'styles/Header.module.scss';

const Login: NextPage = () => {
  return <>
    <header className={styles.next}>
      <h1><Image className={styles.logo} src={logo} alt="Registre" width={120} height={60}/></h1>
    </header>
    <main>
      <h1>Bienvenue sur Registre&#x202F;!</h1>
      <p>Veuillez vous <button onClick={() => signIn ('google')}>connecter avec Google</button>.</p>
    </main>
  </>;
};

export default Login;
