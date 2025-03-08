# Registre

## Introduction

“Registre” is a webapp intended to be used by a family or a group of friends
in order to allow each member to know which movies, books and comics other
members own. Members will register their own titles and be able to search all
registered entries. Thus helping not gifting another member a title they
already have.

## Install and run

### Frontend

The frontend can be configured with the below environment variables:

 - `REGISTRE_BACKEND_URL`: URL to the backend API,
    something like <http://localhost:8080/api> (mandatory).

### Backend

The backend can be configured with the below environment variables

 - `REGISTRE_DB_URL`: URL to the Mongo database,
    optional, defaults to <mongodb://localhost:27017>.
 - `REGISTRE_DB_NAME`: name of the database to use,
    optional, defaults to `registre`.
 - `REGISTRE_DB_USERNAME`: username to authenticate to the DB, optional.
 - `REGISTRE_DB_PASSWORD`: password to authenticate to the DB, optional.
 - `REGISTRE_GOOGLE_CLIENT_ID`: OAuth2 client ID for Google.
 - `REGISTRE_GOOGLE_CLIENT_SECRET`: OAuth2 client secret for Google.
 - `REGISTRE_GOOGLE_REDIRECT_URL`: OAuth2 redirect URL for Google.
    Make sure to set here the URL that users will use, typically points
    to the frontend that will proxy to the backend.

The OAuth2 parameters for Google must be obtained
from the [Google Cloud Console].

[Google Cloud Console]: https://console.cloud.google.com/

## Configuring users

Only users who have their email registered in the database can connect to
Registre. There is no way for them to register themselves. An administrator
must add them with the following MongoDB query:

    db.users.insertOne({name:"<NAME">,emails:["<EMAIL0>","<EMAIL1>"]});

A user can have any number of emails. If a user has none, they cannot log in.
