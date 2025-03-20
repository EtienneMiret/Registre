import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';
import {User} from '@/lib/features/auth/User';

export const registreApiSlice = createApi({
  baseQuery: fetchBaseQuery({baseUrl: '/api/'}),
  reducerPath: 'registreApi',
  tagTypes: ['Users'],
  endpoints: build => ({
    whoAmI: build.query<User, undefined>({
      query: () => 'users/@me',
      providesTags: () => [{type: 'Users', id: '@me'}],
    }),
  }),
});
