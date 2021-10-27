import type { GetServerSidePropsResult, NextPageContext } from 'next'
import Head from 'next/head'
import { getSession, signOut } from 'next-auth/react'
import { Session } from 'next-auth';
import Header from '../components/header';
import Nav from '../components/nav';
import { RegistreLayoutPage } from '../lib/types';

interface HomeParams {
  session: Session
}

const Home: RegistreLayoutPage<HomeParams> = ({session}) => {
  return (
      <>
        <p>Hello {session.user?.email}!</p>
        <p><button onClick={() => signOut()}>Se déconnecter</button></p>
      </>
  );
}

Home.title = 'Accueil';

export async function getServerSideProps (context: NextPageContext): Promise<GetServerSidePropsResult<HomeParams>> {
  const session = await getSession (context);
  if (session === null) {
    return {
      redirect: {
        destination: '/auth/login',
        permanent: false
      }
    }
  }

  return {
    props: {session}
  };
}

export default Home;
