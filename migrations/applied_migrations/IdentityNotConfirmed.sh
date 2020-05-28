#!/bin/bash

echo ""
echo "Applying migration IdentityNotConfirmed"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /identityNotConfirmed                       controllers.IdentityNotConfirmedController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "identityNotConfirmed.title = identityNotConfirmed" >> ../conf/messages.en
echo "identityNotConfirmed.heading = identityNotConfirmed" >> ../conf/messages.en

echo "Migration IdentityNotConfirmed completed"
