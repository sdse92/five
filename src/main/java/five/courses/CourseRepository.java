package five.courses;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<CourseDocument, String> {

    CourseDocument findByName(String name);

    default List<CourseDocument> retrieveByFilter(MongoTemplate mongoTemplate, CourseSearchInvoice invoice, Pageable pageable) {
        Criteria filter = getCriteriaByInvoice(invoice);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(filter),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );
        return mongoTemplate
                .aggregate(aggregation, CourseDocument.class, CourseDocument.class)
                .getMappedResults();
    }

     default Criteria getCriteriaByInvoice(CourseSearchInvoice invoice) {
        Criteria filter = Criteria.where("id").ne("null");
        if (invoice.getId() != null) {
            filter = Criteria.where("id").is(invoice.getId());
        }
        if (invoice.getName() != null) {
            filter.and("name").is(invoice.getName());
        }
        if (invoice.getDescription() != null) {
            filter.and("description").is(invoice.getDescription());
        }
        if (invoice.getFilesDirectory() != null) {
            filter.and("filesDirectory").is(invoice.getFilesDirectory());
        }
        if (invoice.getCourseAdministratorId() != null) {
            filter.and("courseAdministratorId").is(invoice.getCourseAdministratorId());
        }
        if ((invoice.getMembers() != null) && (!invoice.getMembers().isEmpty())) {
            filter.and("members").in(invoice.getMembers());
        }
        if ((invoice.getFileIds() != null) && (!invoice.getFileIds().isEmpty())) {
            filter.and("fileIds").in(invoice.getFileIds());
        }
        return filter;
    }
}
