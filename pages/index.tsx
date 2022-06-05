import type { GetServerSidePropsResult, NextPageContext } from 'next';
import { getSession, signOut } from 'next-auth/react';
import { RegistreLayoutPage, Member } from '../lib/types';

interface HomeParams {
  member: Member
}

const Home: RegistreLayoutPage<HomeParams> = ({member}) => {
  return (
      <>
        <p>Bonjour {member.username}&#x202F;!</p>
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
    props: {member: session.member as Member}
  };
}

export default Home;
