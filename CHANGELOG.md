## Chengelog:
#### version 1.0.0:
- first working version - with rest api, mongodb domain collection, domain model and redis caching. 
#### version 1.0.1:
- added unit tests to LinksService
todo: add integration tests to LinksController and LinksService, to test both api and caching mechanism.
- #### version 2.0.0:
- added CachingService to handle caching mechanism separately from LinksService.
- added integration tests to LinksController. 
- added integration tests to CachingService, using a spy bean of DomainRepository to test number of method calls in the caching mechanism.
- fixed unit tests to LinksService, using CachingService as a mock instead of DomainRepository.
