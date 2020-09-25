#!/bin/bash

echo ""
echo "Applying migration ProblemWithService"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /problemWithService                       controllers.ProblemWithServiceController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "problemWithService.title = problemWithService" >> ../conf/messages.en
echo "problemWithService.heading = problemWithService" >> ../conf/messages.en

echo "Migration ProblemWithService completed"
