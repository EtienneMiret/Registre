import { NextComponentType, NextPage } from 'next';
import { AppPropsType, BaseContext, NextPageContext } from 'next/dist/shared/lib/utils';
import { NextRouter } from 'next/dist/shared/lib/router/router';

export type RegistreSelfLayoutPage<P = {}, IP = P>
    = NextPage<P, IP> & {
  selfLayout: true;
};

export type RegistreLayoutPage<P = {}, IP = P>
    = NextPage<P, IP> & {
  selfLayout?: false;
  title: string;
};

export type RegistrePage<P = {}, IP = P> =
    RegistreLayoutPage<P, IP> |
    RegistreSelfLayoutPage<P, IP>;

export type RegistreAppProps<R extends NextRouter = NextRouter, P = {}>
    = AppPropsType<R, P> & {
  Component: RegistrePage<P, any>
};
