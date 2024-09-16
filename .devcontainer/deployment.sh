#!/usr/bin/env bash

kubectl create namespace log-generator

sed -i "s,{YOUR_DT_URL},$DT_URL,"  /workspaces/$RepositoryName/deployment/LogGenerator.yaml
sed -i "s,{YOUR_DT_LOG_INGEST_TOKEN},$DT_LOG_INGEST_TOKEN,"  /workspaces/$RepositoryName/deployment/LogGenerator.yaml


# Create secret for k6 to use
kubectl -n log-generator create secret generic dt-details \
  --from-literal=DT_ENDPOINT=$DT_URL \
  --from-literal=DT_API_TOKEN=$DT_OPERATOR_TOKEN


kubectl apply -f deployment/LogGenerator.yaml -n log-generator

# Wait for Dynatrace to be ready
#kubectl -n dynatrace wait --for=condition=Ready pod --all --timeout=10m

# Wait 
#kubectl  --for=condition=Ready pod --all --timeout=10m
kubectl -n log-generator log-generator wait --for=jsonpath='{.status.phase}'=Running 

echo "Deployment complete!" 
echo "you can run `kubectl get logs -f -n log-generator log-generator` to see logs shipped to your tenant API"
