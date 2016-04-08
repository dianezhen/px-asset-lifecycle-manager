YOU MUST HAVE TAGS ON YOUR SERVICES.

System expects blob store service to have either 'blobstore' or 'blob-store' tags
System expects asset service to have 'asset' tag

To set ttags on a service using the cf cli

cf update-service <service-name> -t "<tag-name>,<tag-name2>"

