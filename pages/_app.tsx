import '../styles/globals.scss'
import { SessionProvider } from 'next-auth/react'
import { RegistreAppProps } from 'lib/types';
import Layout from 'components/layout';

function MyApp ({Component, pageProps}: RegistreAppProps) {
  if (Component.selfLayout) {
    return <SessionProvider session={pageProps.session}>
      <Component {...pageProps} />
    </SessionProvider>;
  }

  return <SessionProvider session={pageProps.session}>
    <Layout title={Component.title}>
      <Component {...pageProps}/>
    </Layout>
  </SessionProvider>;
}

export default MyApp;
