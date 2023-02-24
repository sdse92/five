package five.fileDescriptors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDescriptorRepository extends MongoRepository<FileDescriptorDocument, String> {
    FileDescriptorDocument findByName(String name);

    default List<FileDescriptorDocument> retrieveByFilter(MongoTemplate mongoTemplate, FileDescriptorSearchInvoice invoice, Pageable pageable) {
        Criteria filter = getCriteriaByInvoice(invoice);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(filter),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );
        return mongoTemplate
                .aggregate(aggregation, FileDescriptorDocument.class, FileDescriptorDocument.class)
                .getMappedResults();
    }

    default Criteria getCriteriaByInvoice(FileDescriptorSearchInvoice invoice) {
        Criteria filter = Criteria.where("id").ne("null");
        if ((invoice.getId() != null) && (!invoice.getId().isEmpty())) {
            filter = Criteria.where("id").in(invoice.getId());
        }
        if (invoice.getName() != null) {
            filter.and("name").is(invoice.getName());
        }
        if ((invoice.getFileType() != null) && !invoice.getFileType().isEmpty()) {
            filter.and("fileType").in(invoice.getFileType());
        }
        if (invoice.getCourseName() != null) {
            filter.and("courseName").is(invoice.getCourseName());
        }
        if (invoice.getStoreLocation() != null) {
            filter.and("storeLocation").is(invoice.getStoreLocation());
        }
        if (invoice.getDownloadUrl() != null) {
            filter.and("downloadUrl").is(invoice.getDownloadUrl());
        }
        if (invoice.getUploadDateFrom() != null && invoice.getUploadDateTo() != null) {
            filter.and("uploadDate").gt(invoice.getUploadDateFrom()).lte(invoice.getUploadDateTo());
        } else if (invoice.getUploadDateFrom() != null) {
            filter.and("uploadDate").gt(invoice.getUploadDateFrom());
        } else if (invoice.getUploadDateTo() != null) {
            filter.and("uploadDate").lte(invoice.getUploadDateTo());
        }
        return filter;
    }
}
