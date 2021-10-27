import { NextComponentType } from 'next';
import { BaseContext, NextPageContext } from 'next/dist/shared/lib/utils';
import Head from 'next/head';
import Header from './header';
import Nav from './nav';

export interface LayoutParams {
  title: string
}

const Layout: NextComponentType<BaseContext, {}, LayoutParams> = ({children, title}) => {
  return <>
    <Head>
      <title>{title} — Registre</title>
      <link rel="icon" href="/icon.png"/>
    </Head>
    <Header/>
    <Nav/>
    {children}
  </>;
}

export default Layout;
