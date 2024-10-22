#!/bin/bash

ACR_HOST=$1
ACR_REPO=$2
ACR_PASSWORD=$3

CHART_NAME=$(grep '^nameOverride:' ./helm-chart/values-local.yaml \
            | awk '{split($0,a,":"); print a[2]}' \
            | awk '{gsub(/^ +| +$/,"")} {print $0 }')

if [[ -z $CHART_NAME ]]
then
  # Read out chart name from helm-chart/Chart.yaml
  CHART_NAME=$(grep 'name:' ./helm-chart/Chart.yaml \
              | awk '{split($0,a,":"); print a[2]}' \
              | awk '{gsub(/^ +| +$/,"")} {print $0 }')
fi

# Define tag based on the latest code commit in your local (please execute git pull to ensure everything is up-to-date)
GIT_COMMIT=$(git log -1 --format=%h)

# Build the docker image inside minikube in local-environment
if [ -z "$ACR_REPO" ]; then
  eval "$(minikube -p minikube docker-env)"
fi

# Create docker image
echo "docker build --no-cache -f Dockerfile -t $CHART_NAME:$GIT_COMMIT ."
docker build --no-cache -f Dockerfile -t "$CHART_NAME":"$GIT_COMMIT" .

# Build and push the docker image to the cloud when ACR_REPO is defined
if [ -n "$ACR_REPO" ]; then
    ACR_REPO=egs-platform--server

  echo "$ACR_PASSWORD" | docker login --username acrplatformprod --password-stdin  "$ACR_HOST"
  docker tag "$CHART_NAME":"$GIT_COMMIT" "$ACR_HOST"/$ACR_REPO:"$GIT_COMMIT"
  docker push "$ACR_HOST"/$ACR_REPO:"$GIT_COMMIT"
  echo "Pushed docker image: $ACR_HOST/$ACR_REPO:$GIT_COMMIT"
  
  # Logout docker
  docker logout
  
  # Clean docker image after pushed completed
  docker system prune -af
fi

echo "Completed!"
