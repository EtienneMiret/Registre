package services

import "time"

type Clock interface {
	Now() time.Time
}

func NewClock() Clock {
	return &SystemClock{}
}

type SystemClock struct{}

func (c *SystemClock) Now() time.Time {
	return time.Now()
}
