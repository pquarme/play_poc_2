package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "actor")
public class Actor extends Model {

	@Id
	public String actor_id;
	public String firstName;
	public String lastName;

	public static Finder<Long, Actor> find = new Finder<Long, Actor>(
			Long.class, Actor.class);
}
