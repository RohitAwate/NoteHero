CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$ BEGIN
	CREATE TYPE UserTier AS ENUM ('FREE', 'PREMIUM', 'ULTIMATE');
EXCEPTION
	WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS Users (
	Username VARCHAR(30) NOT NULL,
	Email VARCHAR(254) NOT NULL,
	Password CHAR(60) NOT NULL,
	Tier UserTier NOT NULL,
	PRIMARY KEY (Username)
);

DO $$ BEGIN
	CREATE TYPE GitHost AS ENUM ('GH', 'GL', 'BB');
EXCEPTION
	WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS Buckets (
	BucketID UUID DEFAULT gen_random_uuid(),
	Username VARCHAR(30) NOT NULL,
	GitHost GitHost NOT NULL,
	HostUsername VARCHAR NOT NULL,
	RepoName VARCHAR NOT NULL,
	Branch VARCHAR NOT NULL,
	LatestBuildID UUID,
	PRIMARY KEY (BucketID),
	FOREIGN KEY (Username) REFERENCES Users (Username)
);

DO $$ BEGIN
	CREATE TYPE BuildStatus AS ENUM ('Success', 'Failed', 'Timed Out', 'Runtime Error');
EXCEPTION
	WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS Builds (
	BuildID UUID DEFAULT gen_random_uuid(),
	BucketID UUID NOT NULL,
	GitBranch VARCHAR NOT NULL,
	CommitHash CHAR(40) NOT NULL,
	StartTime TIME WITH TIME ZONE NOT NULL,
	Status BuildStatus NOT NULL,
	PRIMARY KEY (BuildID),
	FOREIGN KEY (BucketID) REFERENCES Buckets (BucketID)
);

CREATE TABLE IF NOT EXISTS Notes (
	NoteID UUID DEFAULT gen_random_uuid(),
	BucketID UUID NOT NULL,
	HTML TEXT NOT NULL,
	Markdown TEXT NOT NULL,
	Title TEXT NOT NULL,
	Private BOOLEAN NOT NULL DEFAULT TRUE,
	Slug TEXT NOT NULL,
	Categories VARCHAR[],
	PRIMARY KEY (NoteID),
	FOREIGN KEY (BucketID) REFERENCES Buckets (BucketID)
);
