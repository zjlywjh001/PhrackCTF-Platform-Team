CREATE TABLE "banned_ips" (
"id" serial8 NOT NULL,
"ip_addr" text COLLATE "default" NOT NULL,
CONSTRAINT "banned_ips_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "banned_ips" OWNER TO "postgres";

CREATE TABLE "categories" (
"id" serial8 NOT NULL,
"name" text COLLATE "default" NOT NULL,
"mark" text COLLATE "default" NOT NULL,
CONSTRAINT "categories_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "categories" OWNER TO "postgres";

CREATE TABLE "challenges" (
"id" serial8 NOT NULL,
"title" text COLLATE "default" NOT NULL,
"description" text COLLATE "default",
"attachids" text COLLATE "default",
"score" int8 NOT NULL,
"categoryid" int8 NOT NULL,
"flag" text COLLATE "default" NOT NULL,
"exposed" bool NOT NULL DEFAULT false,
"available" timestamp(6) NOT NULL,
"invalidate" timestamp(6) NOT NULL,
"watchby" text COLLATE "default",
CONSTRAINT "challenges_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "challenges" OWNER TO "postgres";

CREATE TABLE "countries" (
"id" serial8 NOT NULL,
"countryname" text COLLATE "default" NOT NULL,
"countrycode" text COLLATE "default" NOT NULL,
CONSTRAINT "countries_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "countries" OWNER TO "postgres";

CREATE TABLE "files" (
"id" serial8 NOT NULL,
"filename" text COLLATE "default" NOT NULL,
"addby" text COLLATE "default" NOT NULL,
"size" int8 NOT NULL,
"md5" text COLLATE "default" NOT NULL,
"challengeid" int8,
"resindex" text COLLATE "default" NOT NULL,
CONSTRAINT "files_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "files" OWNER TO "postgres";

CREATE TABLE "hints" (
"id" serial8 NOT NULL,
"challengeid" int8 NOT NULL,
"content" text COLLATE "default" NOT NULL,
"addtime" timestamp(6) NOT NULL,
CONSTRAINT "hints_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "hints" OWNER TO "postgres";

CREATE TABLE "ip_logs" (
"id" serial8 NOT NULL,
"ipaddr" text COLLATE "default" NOT NULL,
"userid" int8 NOT NULL,
"added" timestamp(6) NOT NULL,
"lastused" timestamp(6) NOT NULL,
"timesused" int8 NOT NULL,
CONSTRAINT "ip_logs_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "ip_logs" OWNER TO "postgres";

CREATE TABLE "news" (
"id" serial8 NOT NULL,
"title" text COLLATE "default" NOT NULL,
"content" text COLLATE "default",
"posttime" timestamp(6) NOT NULL,
CONSTRAINT "news_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "news" OWNER TO "postgres";

CREATE TABLE "operatelog" (
"id" serial8 NOT NULL,
"operatorid" int8 NOT NULL,
"operatefunc" text COLLATE "default" NOT NULL,
"operatetime" timestamp(6) NOT NULL,
"ipaddr" text COLLATE "default" NOT NULL,
CONSTRAINT "operatelog_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "operatelog" OWNER TO "postgres";

CREATE TABLE "passreset" (
"id" serial8 NOT NULL,
"userid" int8 NOT NULL,
"resettoken" text COLLATE "default" NOT NULL,
"used" bool NOT NULL DEFAULT false,
"createtime" timestamp(6) NOT NULL,
"expireson" timestamp(6) NOT NULL,
"usedtime" timestamp(6),
CONSTRAINT "passreset_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "passreset" OWNER TO "postgres";

CREATE TABLE "rules" (
"id" serial8 NOT NULL,
"content" text COLLATE "default",
CONSTRAINT "rules_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "rules" OWNER TO "postgres";

CREATE TABLE "submissions" (
"id" serial8 NOT NULL,
"userid" int8 NOT NULL,
"challenge_id" int8 NOT NULL,
"submit_time" timestamp(6) NOT NULL,
"content" text COLLATE "default" NOT NULL,
"correct" bool NOT NULL DEFAULT false,
CONSTRAINT "submissions_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "submissions" OWNER TO "postgres";

CREATE TABLE "teams" (
"id" serial8 NOT NULL,
"name" text COLLATE "default" NOT NULL,
"teamtoken" text COLLATE "default" NOT NULL,
"score" int8 NOT NULL,
"organization" text,
"description" text,
"isenabled" bool NOT NULL DEFAULT true,
"createtime" timestamp NOT NULL,
"countryid" int8 NOT NULL,
CONSTRAINT "teams_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "teams" OWNER TO "postgres";

CREATE TABLE "users" (
"id" serial8 NOT NULL,
"username" text COLLATE "default" NOT NULL,
"password" text COLLATE "default" NOT NULL,
"email" text COLLATE "default" NOT NULL,
"phone" text COLLATE "default",
"organization" text COLLATE "default",
"countryid" int8 NOT NULL,
"description" text COLLATE "default",
"isenabled" bool NOT NULL DEFAULT true,
"score" int8 NOT NULL DEFAULT 0,
"token" text COLLATE "default",
"salt" text COLLATE "default" NOT NULL,
"regtime" timestamp(6) NOT NULL,
"lastactive" timestamp(6),
"role" varchar(10) COLLATE "default" NOT NULL DEFAULT "current_user"(),
"teamid" int8,
CONSTRAINT "users_pkey" PRIMARY KEY ("id") 
)
WITHOUT OIDS;

ALTER TABLE "users" OWNER TO "postgres";

CREATE TABLE "config" (
"id" int8 NOT NULL,
"varname" text NOT NULL,
"varvalue" text NOT NULL,
PRIMARY KEY ("id") 
)
WITHOUT OIDS;


ALTER TABLE "challenges" ADD CONSTRAINT "fk_Challenges_Categories_1" FOREIGN KEY ("categoryid") REFERENCES "categories" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "files" ADD CONSTRAINT "challengefile" FOREIGN KEY ("challengeid") REFERENCES "challenges" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "hints" ADD CONSTRAINT "challengeid" FOREIGN KEY ("challengeid") REFERENCES "challenges" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "ip_logs" ADD CONSTRAINT "fk_ip_logs_Users_1" FOREIGN KEY ("userid") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "operatelog" ADD CONSTRAINT "userid" FOREIGN KEY ("operatorid") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "passreset" ADD CONSTRAINT "userid" FOREIGN KEY ("userid") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "submissions" ADD CONSTRAINT "uid" FOREIGN KEY ("userid") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "submissions" ADD CONSTRAINT "fk_Submissions_Challenges_1" FOREIGN KEY ("challenge_id") REFERENCES "challenges" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "users" ADD CONSTRAINT "team" FOREIGN KEY ("teamid") REFERENCES "teams" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "users" ADD CONSTRAINT "fk_Users_Countries_1" FOREIGN KEY ("countryid") REFERENCES "countries" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "teams" ADD CONSTRAINT "leader_fk" FOREIGN KEY ("countryid") REFERENCES "countries" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
INSERT INTO config (id, varname, varvalue) VALUES (1, 'max_team_members', '3');INSERT INTO config (id, varname, varvalue) VALUES (2, 'comp_start', '');INSERT INTO config (id, varname, varvalue) VALUES (3, 'comp_end', '');INSERT INTO rules (id, content) VALUES (1, '<p></p>');---- TOC entry 2190 (class 0 OID 0)-- Dependencies: 200-- Name: rules_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres--SELECT pg_catalog.setval('rules_id_seq', 1, false);
