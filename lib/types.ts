import { NextPage } from 'next';
import { AppPropsType } from 'next/dist/shared/lib/utils';
import { NextRouter } from 'next/dist/shared/lib/router/router';
import { ObjectID } from 'bson';

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

export type Member = {
  _id: ObjectID;
  username: string;
  accessGranted: boolean;
  emails: string[];
}
