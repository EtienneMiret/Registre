import NextAuth from 'next-auth';
import GoogleProvider from 'next-auth/providers/google';
import { MongoDBAdapter } from '@next-auth/mongodb-adapter';
import database from 'lib/mongo/db';
import { findByEmail } from '../../../lib/mongo/members/find';

export default NextAuth ({
  providers: [
      GoogleProvider({
        clientId: process.env.GOOGLE_CLIENT_ID!,
        clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
      })
  ],
  callbacks: {
    async signIn({user}) {
      if (!user.email) {
        return false;
      }
      
      return await findByEmail(user.email)
          .then(u => {
            return u !== null && u.accessGranted
          });
    },
    async session({session, user}) {
      session.member = await findByEmail(user.email!);
      return session;
    },
  },
  adapter: MongoDBAdapter({
    db: (await database)
  })
});
