import Image from 'next/image';
import logo from 'public/logo.png';
import { NextPage, NextPageContext } from 'next';
import { getSession, signIn } from 'next-auth/react';
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

export async function getServerSideProps (context: NextPageContext) {
  const session = await getSession (context);
  if (session !== null) {
    return {
      redirect: {
        destination: '/',
        permanent: false
      }
    }
  }

  return {
    props: {}
  };
}

export default Login;
