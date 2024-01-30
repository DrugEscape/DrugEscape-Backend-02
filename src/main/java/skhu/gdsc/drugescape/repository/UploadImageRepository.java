package skhu.gdsc.drugescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skhu.gdsc.drugescape.domain.UploadImage;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
