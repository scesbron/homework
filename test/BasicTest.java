import models.Commit;
import models.Repository;
import models.LightRepository;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.junit.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import play.test.*;
import utils.DateTimeAdapter;
import utils.Page;

public class BasicTest extends UnitTest {

	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Test
    public void addCommitsToRepository() throws Exception {
    	List<Commit> commits = new ArrayList<Commit>();
    	User user = new User("scesbron@gmail.com", "scesbron", "Sébastien Cesbron");
    	commits.add(new Commit(user, formatter.parseDateTime("2012-05-21 09:56:24")));
    	commits.add(new Commit(user, formatter.parseDateTime("2012-05-21 10:20:40")));
    	commits.add(new Commit(user, formatter.parseDateTime("2012-05-21 10:31:47")));
    	commits.add(new Commit(user, formatter.parseDateTime("2012-05-21 11:59:44")));
    	commits.add(new Commit(user, formatter.parseDateTime("2012-05-23 13:29:17")));

    	Repository repo = new Repository();
    	repo.commits = commits;

    	assertEquals(1, repo.contributors.size());
    	assertNotNull(repo.commits);
    	assertEquals(5, repo.commits.size());
    	assertNotNull(repo.contributors.get(0).commits);
    	assertEquals(5, repo.contributors.get(0).commits.size());

    	Map<Date, Integer> repartition = repo.getCommitRepartitionByDate();
    	Iterator<Entry<Date, Integer>> itr = repartition.entrySet().iterator();
    	Entry<Date, Integer> entry = itr.next();
    	assertEquals(new LocalDate(2012, 05, 21), new LocalDate(entry.getKey()));
    	assertEquals(4, entry.getValue().intValue());
    	entry = itr.next();
    	assertEquals(new LocalDate(2012, 05, 22), new LocalDate(entry.getKey()));
    	assertEquals(0, entry.getValue().intValue());
    	entry = itr.next();
    	assertEquals(new LocalDate(2012, 05, 23), new LocalDate(entry.getKey()));
    	assertEquals(1, entry.getValue().intValue());
    }

    @Test
    public void getUserName() {
    	User user = new User("email@email.com", "login", null);
    	assertNotNull(user.email);
    	assertNotNull(user.login);
    	assertNotNull(user.name);
    	assertEquals(user.login, user.name);
    }

    @Test
    public void testParseDate() {
    	JsonElement element = new JsonPrimitive("2012-05-25T02:56:30-07:00");
    	DateTimeAdapter deserializer = new DateTimeAdapter();
    	DateTime date = deserializer.deserialize(element, DateTime.class, null);
    	assertNotNull(date);
    	assertEquals(2012, date.getYear());
    }
}
