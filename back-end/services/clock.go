package services

import "time"

type Clock interface {
	Now() time.Time
}
