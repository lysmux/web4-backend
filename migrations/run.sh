#!/bin/bash
/liquibase/docker-entrypoint.sh \
  --defaults-file=/liquibase/liquibase.properties update \
  --url="${LIQUIBASE_URL}" \
  --username="${LIQUIBASE_USERNAME}" \
  --password="${LIQUIBASE_PASSWORD}"