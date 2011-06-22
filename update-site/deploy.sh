#!/bin/bash

DATE=`date +%Y-%m-%d`
BASE_PATH="/cse/www2/types/checker-framework/checker-plugin"
SITE_NAME="update-site-$DATE"
DEPLOY_PATH="$BASE_PATH/$SITE_NAME"
SERVER="jicama.cs.washington.edu"

scp -r . $SERVER:$DEPLOY_PATH
ssh $SERVER "cd $BASE_PATH; rm update-site; ln -s $SITE_NAME update-site"
