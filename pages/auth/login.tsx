import Image from 'next/image';
import logo from 'public/logo.png';
import { GetServerSidePropsResult, NextPageContext } from 'next';
import { getSession, signIn } from 'next-auth/react';
import styles from 'styles/Header.module.scss';
import { RegistreSelfLayoutPage } from 'lib/types';

const Login: RegistreSelfLayoutPage = () => {
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

Login.selfLayout = true;

export async function getServerSideProps (context: NextPageContext): Promise<GetServerSidePropsResult<{}>> {
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
