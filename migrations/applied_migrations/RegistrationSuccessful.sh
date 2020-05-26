#!/bin/bash

echo ""
echo "Applying migration RegistrationSuccessful"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /registrationSuccessful                       controllers.RegistrationSuccessfulController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "registrationSuccessful.title = registrationSuccessful" >> ../conf/messages.en
echo "registrationSuccessful.heading = registrationSuccessful" >> ../conf/messages.en

echo "Migration RegistrationSuccessful completed"
