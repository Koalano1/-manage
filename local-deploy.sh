#!/bin/bash
PROJECT=$1
REGISTRY=$2
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

RELEASE_NAME=$CHART_NAME

# Build project
mvn clean package -Dmaven.test.skip
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failed to build the application'; exit $rc
fi

# Define tag based on the latest code commit in your local (please execute git pull to ensure everything is up-to-date)
GIT_COMMIT=$(git log -1 --format=%h)

# Create docker image
docker build --no-cache -f Dockerfile -t "$REGISTRY"/"$PROJECT"/"$CHART_NAME":"$GIT_COMMIT" .
docker push "$REGISTRY"/"$PROJECT"/"$CHART_NAME":"$GIT_COMMIT"

echo "helm -n $PROJECT upgrade --install $RELEASE_NAME ./helm-chart -f ./helm-chart/values-local.yaml --set image.repository=$REGISTRY/$PROJECT --set image.tag=$GIT_COMMIT"
helm upgrade -n "$PROJECT" --install "$RELEASE_NAME" ./helm-chart -f ./helm-chart/values-local.yaml --set image.repository="$REGISTRY"/"$PROJECT" --set image.tag="$GIT_COMMIT"
kubectl rollout restart -n "$PROJECT" deployment "$RELEASE_NAME"
# NOTE: docker system prune can make deployment failed because it cleans up istio-init container
# Reference: https://www.alibabacloud.com/help/en/alibaba-cloud-service-mesh/latest/why-the-pod-in-init-crash-of-the-state

echo "Completed!"