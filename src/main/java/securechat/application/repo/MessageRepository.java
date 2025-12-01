package securechat.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import securechat.application.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>
{

}
