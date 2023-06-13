import { Box, Grid, Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { Link } from 'react-router-dom';
import PostIconList from 'src/components/Post/PostIconGroup/PostIconList';
import TweetPost from 'src/UI/TweetPost';
import CommentsList from 'src/components/Comments/CommentsList';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { getTweetByID } from 'src/redux/selectors/selectors';
import { getTweetById } from 'src/redux/thunk/getTweetById';

import Retweet from './Retweet';
import { useMode } from 'src/styles/_materialTheme';

function TweetPage() {
  const { id } = useParams();
  const user = useSelector((state) => state.user.user) || '';
  const dispatch = useDispatch();

  const theme = useMode();
  //getting single tweet
  useEffect(() => {
    dispatch(getTweetById(id));
  }, [id]);
  const tweet = useSelector(getTweetByID);
  const post = tweet.tweet;
  /////////////////
  //Like tweet
  // useEffect(() => {
  //   dispatch(likePost(id));
  // }, [post.countLikes]);

  return (
    <Box
      sx={{
        borderLeft: {
          xs: 'none',
          sm: `1px solid ${theme.palette.border.main}`,
        },
        borderRight: {
          xs: 'none',
          sm: `1px solid ${theme.palette.border.main}`,
        },
      }}
    >
      {/* back to home page */}
      <Link to="/">
        <Box
          sx={{
            color: `${theme.palette.text.primary}`,
            pt: '15px',
            display: 'flex',
            gap: '10px',
            alignItems: 'center',
          }}
        >
          <ArrowBackIcon />
          <Typography variant="h6" fontWeight={800}>
            Tweet
          </Typography>
        </Box>
      </Link>

      {post && (
        <TweetPost
          id={post.tweetId}
          displayName={user.fullName}
          text={post.body}
          username={post.userTag}
          logoUrl={post.userAvatarImage}
          verified={user.isVerified}
          image={post.attachmentsImages[0]}
          likes={post.countLikes}
          reply={post.countReply}
          retweet={post.countRetweets}
        />
      )}
      <Grid
        container
        alignItems="center"
        justifyContent="center"
        sx={{
          borderBottom: `1px solid ${theme.palette.border.main}`,
          pb: '20px',
        }}
      >
        {post && (
          <PostIconList
            likes={post.countLikes}
            reply={post.countReply}
            retweet={post.countRetweets}
          />
        )}
      </Grid>
      <Retweet />
      <CommentsList />
    </Box>
  );
}

export default TweetPage;
