FROM liquibase/liquibase:4.31

USER liquibase

COPY --chown=liquibase:liquibase migrations/changelog /liquibase/changelog
COPY --chown=liquibase:liquibase migrations/master.xml /liquibase/master.xml
COPY --chown=liquibase:liquibase migrations/liquibase.properties /liquibase/liquibase.properties
COPY --chown=liquibase:liquibase migrations/run.sh /liquibase/run.sh

RUN chmod +x /liquibase/run.sh

WORKDIR /liquibase

ENTRYPOINT ["./run.sh"]