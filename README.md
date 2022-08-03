# Registre

## Introduction

“Registre” is a webapp intended to be used by a family or a group of friends
in order to allow each member to know which movies, books and comics other
members own. Members will register their own titles and be able to search all
registered entries. Thus helping not gifting another member a title they
already have.

## Test locally

1. Create an `.env.development.local` file with:
    - GOOGLE_CLIENT_ID=xxxxx
    - GOOGLE_CLIENT_SECRET=xxxxx
    - MONGODB_URI=mongodb://127.0.0.1:27017
2. Have a mongo server listening:
    - `docker run -d -p 127.0.0.1:27017:27017 mongo`.
3. Insert the required users:
    - connect to mongo `registre` db
    - `db.members.insertOne({username:"Étienne", accessGranted: true, emails:["etienne.miret@ens-lyon.org"]});`
4. Launch the Next.js server:
    - `yarn dev`
5. Connect to http://localhost:3000.
