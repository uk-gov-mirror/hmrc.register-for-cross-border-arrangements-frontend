#!/bin/bash

echo ""
echo "Applying migration UnsuccessfulSubscription"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /unsuccessfulSubscription                       controllers.UnsuccessfulSubscriptionController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "unsuccessfulSubscription.title = unsuccessfulSubscription" >> ../conf/messages.en
echo "unsuccessfulSubscription.heading = unsuccessfulSubscription" >> ../conf/messages.en

echo "Migration UnsuccessfulSubscription completed"
