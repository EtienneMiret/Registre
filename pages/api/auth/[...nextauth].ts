import NextAuth from 'next-auth';
import GoogleProvider from 'next-auth/providers/google';
import { MongoDBAdapter } from '@next-auth/mongodb-adapter';
import database from 'lib/mongo/db';

export default NextAuth ({
  providers: [
      GoogleProvider({
        clientId: process.env.GOOGLE_CLIENT_ID!,
        clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
      })
  ],
  callbacks: {
    async signIn({user}) {
      return await database.then(db => db.collection('members'))
          .then(users => users.findOne({emails: user.email}))
          .then(u => {
            return u !== null && u.accessGranted === true
          });
    },
    async session({session, user}) {
      session.member = await database.then(db => db.collection('members'))
          .then(members => members.findOne({emails: user.email}));
      return session;
    },
  },
  adapter: MongoDBAdapter({
    db: (await database)
  })
});
