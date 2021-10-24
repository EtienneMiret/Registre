import NextAuth from 'next-auth';
import GoogleProvider from 'next-auth/providers/google';
import { MongoDBAdapter } from '@next-auth/mongodb-adapter';
import mongoClient from 'lib/mongo-client';

export default NextAuth ({
  providers: [
      GoogleProvider({
        clientId: process.env.GOOGLE_CLIENT_ID!,
        clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
      })
  ],
  adapter: MongoDBAdapter ({
    db: (await mongoClient).db('registre')
  })
});
