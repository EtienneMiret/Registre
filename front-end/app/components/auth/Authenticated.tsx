'use client';

import {ReactNode} from 'react';
import {useWhoAmIQuery} from '@/lib/features/auth/authSlice';
import Link from 'next/link';

interface Props {
  readonly children: ReactNode;
}

export const Authenticated = ({children}: Props) => {
  const { isError, isLoading, isSuccess } = useWhoAmIQuery(undefined);

  if (isLoading) {
    return 'Loading…';
  }
  if (isError) {
    const login = new URL('/api/auth/login/google', window.location.href);
    login.searchParams.set('redirectTo', window.location.href);
    return <>
      Please <Link href={login}>log in</Link>.
    </>;
  }
  return children;
}
