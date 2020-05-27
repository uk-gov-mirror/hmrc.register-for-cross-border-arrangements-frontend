#!/bin/bash

echo ""
echo "Applying migration IdentityConfirmed"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /identityConfirmed                       controllers.IdentityConfirmedController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "identityConfirmed.title = identityConfirmed" >> ../conf/messages.en
echo "identityConfirmed.heading = identityConfirmed" >> ../conf/messages.en

echo "Migration IdentityConfirmed completed"
